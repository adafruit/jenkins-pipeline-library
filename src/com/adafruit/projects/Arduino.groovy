package com.adafruit.projects;

def runOnNode(String repo, String label) {

  node(label) {

    currentBuild.result = "SUCCESS"

    try {

       stage 'Clone'

         git url: repo

       stage 'Test'

         lock(env.NODE_NAME + "-" + label) {
           sh 'prove --formatter TAP::Formatter::Jenkins /home/pi/test.t'
         }

       stage 'Publish'

         step([$class: "TapPublisher", testResults: "**/target/tap-unit.log"])


    } catch (err) {

        currentBuild.result = "FAILURE"

        throw err

    }

  }

}
