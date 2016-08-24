def call(String repo, String[] labels) {

  for(label in labels) {
    testBoard(repo, label)
  }

}
