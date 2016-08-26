def call(libraries) {
  for(library in libraries) {
    sh "arduino --install-library '${library}' || true"
  }
}
