def call(boards) {

  stage "Verify Examples"

  for(board in boards) {

    // bind the label variable before the closure
    def platform = board
    def examples = []
    def example_names = []

    node(platform) {

      checkout scm
      sh "arduino --board \$${platform} --save-prefs"

      examples = sh(returnStdout: true, script: "find examples -name '*.ino' | sort").trim().split("\n")

      for (int i = 0; i < examples.size(); i++) {
        example_names[i] = sh(returnStdout: true, script: "basename ${examples[i]} .ino")
      }

    }

    def builders = [:]

    for (int i = 0; i < examples.size(); i++) {

      def example = examples[i]

      builders[example_names[i]] {

        node(platform) {
          echo "Verifying ${example_names[i]}.ino on ${platform}"
          sh "arduino --board \$${platform} --verify ${example} 2>&1"
        }

      }

    }

    parallel builders

  }

}
