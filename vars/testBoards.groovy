def call(boards) {

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
        def test = tests[i]

        echo "Testing ${name}.ino on ${platform}"
        sh "arduino --board \$${platform} --port \$${platform}_PORT --upload ${test}"
        sh "prove --formatter TAP::Formatter::Jenkins tests/arduino_serial.t :: \$${platform}_PORT"

      }

    }

  }

}
