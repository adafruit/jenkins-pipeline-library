def call(String[] boards) {
  for(board in boards) {
    sh "arduino --install-boards \$${board} 2>&1"
  }
}
