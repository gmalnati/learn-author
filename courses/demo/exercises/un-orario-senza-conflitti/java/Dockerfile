# syntax=docker/dockerfile:1
# Builds ./${SRC} (starter or solution) with the toolchain image of the edition: internal tests
# (learn-test), service (learn-build), started by learn-run on port 8080.
ARG REGISTRY=ghcr.io/gmalnati
FROM ${REGISTRY}/learn-toolchain-java-web:2026.3
# The code runs as the unprivileged user of the toolchain image, never as root.
USER learn
WORKDIR /workspace
COPY --chown=learn:learn . .
# The Compose project of the check, which names the caches of this project.
ARG CACHE_ID=learn-exercise
# Build caches (ADR 027): what Gradle keeps of this project between checks (build/, .gradle/), one
# per project. Not the Gradle home: shared by builds at the same time, it slowed them down.
RUN --mount=type=cache,id=${CACHE_ID}-build,target=/workspace/build,uid=10001,gid=10001,sharing=private \
    --mount=type=cache,id=${CACHE_ID}-gradle,target=/workspace/.gradle,uid=10001,gid=10001,sharing=private \
    learn-test && learn-build
EXPOSE 8080
CMD ["learn-run"]
