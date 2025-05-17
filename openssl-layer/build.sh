#!/bin/bash
set -e

# === Configurable variables ===
LAYER_NAME="openssl-layer"
OPENSSL_VERSION="1.1.1w"

# Optional build path (default to current directory)
BUILD_DIR="${1:-$(pwd)}"
ZIP_NAME="layer.zip"

echo "🔧 Build path: $BUILD_DIR"

# Check OpenSSL tarball
OPENSSL_TARBALL="$BUILD_DIR/openssl-${OPENSSL_VERSION}.tar.gz"
if [ ! -f "$OPENSSL_TARBALL" ]; then
  echo "❌ ERROR: OpenSSL tarball not found at $OPENSSL_TARBALL"
  echo "Please download it from https://www.openssl.org/source/"
  exit 1
fi

# Build Docker image
docker build \
  --build-arg OPENSSL_VERSION=$OPENSSL_VERSION \
  -t lambda-openssl-builder \
  -f "$BUILD_DIR/Dockerfile" "$BUILD_DIR"

# Prepare output folder
mkdir -p "$BUILD_DIR"

# Extract built zip from container
CONTAINER_ID=$(docker create lambda-openssl-builder)
docker cp "$CONTAINER_ID:/tmp/$ZIP_NAME" "$BUILD_DIR/"
docker rm "$CONTAINER_ID"

echo "✅ Layer built: $BUILD_DIR/$ZIP_NAME"

