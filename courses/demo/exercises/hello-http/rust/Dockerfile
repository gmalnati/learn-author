# syntax=docker/dockerfile:1
# Builds ./${SRC} (starter or solution) with the toolchain image of the edition: internal tests
# (learn-test), service (learn-build), started by learn-run on port 8080.
ARG REGISTRY=ghcr.io/gmalnati
FROM ${REGISTRY}/learn-toolchain-rust-web:2026.3 AS toolchain
FROM toolchain
# The code runs as the unprivileged user of the toolchain image, never as root.
USER learn
WORKDIR /workspace
COPY --chown=learn:learn . .
# Build cache (ADR 027): what cargo compiles stays between checks, one cache for every exercise.
# Cargo compiles again a file newer than the last build, so every file gets the current time: a file
# put back with an older one is compiled too.
RUN --mount=type=cache,id=learn-toolchain-rust-web-2026.3-target,target=/opt/learn/target,from=toolchain,source=/opt/learn/target,uid=10001,gid=10001,sharing=private \
    find . -type f -exec touch {} + && learn-test && learn-build
EXPOSE 8080
CMD ["learn-run"]
