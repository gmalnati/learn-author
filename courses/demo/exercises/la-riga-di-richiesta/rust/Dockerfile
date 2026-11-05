# syntax=docker/dockerfile:1
# Builds ./${SRC} (starter or solution) with the toolchain image of the edition (learn-build) and
# runs the program on the cases of cases.yaml (learn-check-cases, which starts it with learn-run).
ARG REGISTRY=ghcr.io/gmalnati
FROM ${REGISTRY}/learn-check-cases:2026.3 AS cases
FROM ${REGISTRY}/learn-toolchain-rust:2026.3 AS toolchain
FROM toolchain
COPY --from=cases /usr/local/bin/learn-check-cases /usr/local/bin/learn-check-cases
# The code runs as the unprivileged user of the toolchain image, never as root.
USER learn
WORKDIR /workspace
COPY --chown=learn:learn . .
# Build cache (ADR 027): what cargo compiles stays between checks, one cache for every exercise.
# Cargo compiles again a file newer than the last build, so every file gets the current time: a file
# put back with an older one is compiled too.
# The cases are mounted only while the program runs on them: they are not part of the project.
RUN --mount=type=cache,id=learn-toolchain-rust-2026.3-target,target=/opt/learn/target,from=toolchain,source=/opt/learn/target,uid=10001,gid=10001,sharing=private \
    --mount=type=bind,from=exercise,source=cases.yaml,target=/opt/learn/cases.yaml \
    find . -type f -exec touch {} + && learn-build && learn-check-cases /opt/learn/cases.yaml
