def call(boards) {

  stage "Verify Examples"

  for(board in boards) {

    // bind the label variable before the closure
    def platform = board
    def examples = []

    node(platform) {
      checkout scm
      sh "arduino --board \$${platform} --save-prefs"
      examples = sh(returnStdout: true, script: "find examples -name '*.ino' | sort").trim().split("\n")
    }

    def builders = [:]

    for (int i = 0; i < examples.size(); i++) {

      def example = sh(returnStdout: true, script: "basename ${examples[i]} .ino")

      builders[example] {

        node(platform) {
          echo "Verifying ${example} on ${platform}"
          sh "arduino --board \$${platform} --verify ${example} 2>&1"
        }

      }

    }

    parallel builders

  }

}
