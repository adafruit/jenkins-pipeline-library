def call(boards) {
  for(board in boards) {
    echo "installing: ${board}"
    sh "arduino --install-boards ${board} || true"
  }
}
