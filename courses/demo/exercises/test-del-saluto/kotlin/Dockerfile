# syntax=docker/dockerfile:1
# Builds ./${SRC} (starter or solution) with the toolchain image of the edition: internal tests
# (learn-test), service (learn-build), started by learn-run on port 8080.
ARG REGISTRY=ghcr.io/gmalnati
FROM ${REGISTRY}/learn-toolchain-kotlin-web:2026.3
# The code runs as the unprivileged user of the toolchain image, never as root.
USER learn
WORKDIR /workspace
COPY --chown=learn:learn . .
# The Compose project of the check, which names the caches of this project.
ARG CACHE_ID=learn-exercise
# Build caches (ADR 027): what Gradle keeps of this project between checks (build/, .gradle/,
# .kotlin/), one per project. Not the Gradle home: shared by builds at the same time, it slowed
# them down.
# Tests exercise (ADR 024): the faulty versions of mutants/ are mounted only while the tests of
# the student run again on each of them (learn-test-mutants).
RUN --mount=type=cache,id=${CACHE_ID}-build,target=/workspace/build,uid=10001,gid=10001,sharing=private \
    --mount=type=cache,id=${CACHE_ID}-gradle,target=/workspace/.gradle,uid=10001,gid=10001,sharing=private \
    --mount=type=cache,id=${CACHE_ID}-kotlin,target=/workspace/.kotlin,uid=10001,gid=10001,sharing=private \
    --mount=type=bind,from=mutants,target=/opt/learn/mutants \
    learn-test && learn-test-mutants && learn-build
EXPOSE 8080
CMD ["learn-run"]
