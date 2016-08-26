def call(libraries) {
  for(library in libraries) {
    sh "arduino --install-library '${library}' 2>&1"
  }
}
