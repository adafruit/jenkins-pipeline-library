package com.adafruit.platforms;

def verifyExamples(boards) {

  for(board in boards) {

    stage "Verify Examples: ${board}"

    // bind the label variable before the closure
    def platform = board
    def examples = []

    node(platform) {

      sh "arduino --board \$${platform} --save-prefs"

      examples = sh(returnStdout: true, script: "find examples -name '*.ino' | sort").trim().split("\n")

      for(int i = 0; i < examples.size(); i++) {

        def name = sh(returnStdout: true, script: "basename ${examples[i]} .ino").trim()
        def example = examples[i]

        echo "Verifying ${name}.ino on ${platform}"
        sh "arduino --board \$${platform} --verify ${example}"

      }

    }

  }

}

def testBoards(boards) {

  for(board in boards) {

    stage "Test: ${board}"

    // bind the label variable before the closure
    def platform = board
    def tests = []

    node(platform) {

      sh "arduino --board \$${platform} --save-prefs"
      sh "cp -r .tests tests"

      tests = sh(returnStdout: true, script: "find tests -name '*.ino' | sort").trim().split("\n")

      for(int i = 0; i < tests.size(); i++) {

        def name = sh(returnStdout: true, script: "basename ${tests[i]} .ino").trim()
        def path = tests[i]
        def test = this.generateTest(path, platform)

        echo "Testing ${name}.ino on ${platform}"
        sh "arduino --board \$${platform} --port \$${platform}_PORT --upload ${test}"
        sh "prove --formatter TAP::Formatter::Jenkins ${test} :: \$${platform}_PORT || true"

      }

    }

  }

}

def publishResults(boards) {

  stage "Publish Results"

  def _nodes = this.getNodeNames(boards)

  for(int i = 0; i < _nodes.size(); i++) {

    def name = _nodes[i]

    node(name) {
      step([$class: 'TapPublisher', discardOldReports: false, enableSubtests: false, failIfNoResults: false, failedTestsMarkBuildAsFailure: true, flattenTapResult: false, includeCommentDiagnostics: false, outputTapToConsole: true, planRequired: true, showOnlyFailures: false, skipIfBuildNotOk: false, stripSingleParents: false, testResults: '**/*.tap', todoIsFailure: false, validateNumberOfTests: true, verbose: true])
    }

  }

  node('master') {
    step([$class: 'GitHubCommitStatusSetter'])
  }

}

def installLibraries(libraries) {
  for(library in libraries) {
    sh "arduino --install-library '${library}' || true"
  }
}

def installDependencies(config) {

  def completed = []

  node('master') {
    step([$class: 'GitHubSetCommitStatusBuilder'])
  }

  def _nodes = this.getNodeNames(config.boards)

  for(int i = 0; i < _nodes.size(); i++) {

    def name = _nodes[i]

    if(! (name in completed)) {

      node(name) {
        stage "Setup: ${name}"
        step([$class: 'WsCleanup', deleteDirs: true, notFailBuild: true, patterns: [[pattern: '*.tap', type: 'INCLUDE'], [pattern: 'tests', type: 'INCLUDE']]])
        checkout scm
        env.PATH = '$HOME/arduino_ide:$PATH'
        this.installBoards(config.platforms)
        this.installLibraries(config.libraries)
      }

      completed << name

    }

  }

}

def installBoards(boards) {
  for(board in boards) {
    echo "installing: ${board}"
    sh "arduino --install-boards ${board} || true"
  }
}

def getNodeNames(labels) {

  def result = []

  for(label in labels) {

     def _label = Jenkins.instance.getLabel(label)
     def _nodes = _label.getNodes()

     for(_node in _nodes) {

       if(! (_node in result)) {
         result << _node.getNodeName()
       }

     }

  }

  return result

}

def generateTest(path, platform) {

  def test = "${path}.${platform}.t"

  writeFile file: test, text: """#!/usr/bin/env node

  var Gpio = require('onoff').Gpio,
  reset = new Gpio(18, 'out');
  var SerialPort = require('serialport');
  var args = process.argv.slice(2);
  var started = false;

  reset.writeSync(0);
  reset.writeSync(1);

  setTimeout(function() {
    console.error('# test timeout');
    process.exit(1);
  }, 30000);

  var port = new SerialPort(args[0], {
    parser: SerialPort.parsers.readline('\\r\\n'),
    baudRate: 115200
  });

  port.on('error', function(err) {
    console.error(err);
    process.exit(1);
  });

  port.on('data', function(data) {

    if(/Starting Tests/i.test(data)) {
      started = true;
    } else if(/Done with Tests/i.test(data)) {
      console.log(data);
      process.exit();
    }

    if(started)
      console.log(data);

  });
  """

  return test

}
