# syntax=docker/dockerfile:1
# Runs the internal tests of ./${SRC} (starter or solution) with the toolchain image of the
# edition (learn-test): the exercise is not a service, and learn check reads the reports.
ARG REGISTRY=ghcr.io/gmalnati
FROM ${REGISTRY}/learn-toolchain-rust:2026.3 AS toolchain
FROM toolchain
# The code runs as the unprivileged user of the toolchain image, never as root.
USER learn
WORKDIR /workspace
COPY --chown=learn:learn . .
# Build cache (ADR 027): what cargo compiles stays between checks, one cache for every exercise.
# Cargo compiles again a file newer than the last build, so every file gets the current time: a file
# put back with an older one is compiled too.
# Tests exercise (ADR 024): the faulty versions of mutants/ are mounted only while the tests of
# the student run again on each of them (learn-test-mutants).
RUN --mount=type=cache,id=learn-toolchain-rust-2026.3-target,target=/opt/learn/target,from=toolchain,source=/opt/learn/target,uid=10001,gid=10001,sharing=private \
    --mount=type=bind,from=mutants,target=/opt/learn/mutants \
    find . -type f -exec touch {} + && learn-test && learn-test-mutants
