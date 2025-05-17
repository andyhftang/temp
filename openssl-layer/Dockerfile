FROM public.ecr.aws/amazonlinux/amazonlinux:2

ARG OPENSSL_VERSION
WORKDIR /tmp

# Install build tools
RUN yum install -y \
    gcc \
    make \
    perl \
    tar \
    wget \
    zip \
    gzip \
    zlib-devel

# Add OpenSSL source to container
COPY openssl-${OPENSSL_VERSION}.tar.gz ./

# Extract and build OpenSSL
RUN tar -xzf openssl-${OPENSSL_VERSION}.tar.gz

RUN cd openssl-${OPENSSL_VERSION} && \
    ./config --prefix=/opt/openssl --openssldir=/opt/openssl shared zlib && \
    make -j$(nproc) && \
    make install

# Prepare layer
RUN mkdir -p /tmp/layer && \
    mkdir -p /tmp/layer/lib && \
    if [ -d /opt/openssl/lib64 ]; then cp -r /opt/openssl/lib64/* /tmp/layer/lib/; fi && \
    cp -r /opt/openssl/lib /tmp/layer/lib/ && \
    cp -r /opt/openssl/bin /tmp/layer/ && \
    cp -r /opt/openssl/include /tmp/layer/

# Zip layer content
RUN cd /tmp/layer && \
    zip -r /tmp/layer.zip .
