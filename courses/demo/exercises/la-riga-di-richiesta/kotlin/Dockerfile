# syntax=docker/dockerfile:1
# Builds ./${SRC} (starter or solution) with the toolchain image of the edition (learn-build) and
# runs the program on the cases of cases.yaml (learn-check-cases, which starts it with learn-run).
ARG REGISTRY=ghcr.io/gmalnati
FROM ${REGISTRY}/learn-check-cases:2026.3 AS cases
FROM ${REGISTRY}/learn-toolchain-kotlin:2026.3
COPY --from=cases /usr/local/bin/learn-check-cases /usr/local/bin/learn-check-cases
# The code runs as the unprivileged user of the toolchain image, never as root.
USER learn
WORKDIR /workspace
COPY --chown=learn:learn . .
# The Compose project of the check, which names the caches of this project.
ARG CACHE_ID=learn-exercise
# Build caches (ADR 027): what Gradle keeps of this project between checks (build/, .gradle/,
# .kotlin/), one per project. Not the Gradle home: shared by builds at the same time, it slowed
# them down.
# The cases are mounted only while the program runs on them: they are not part of the project.
RUN --mount=type=cache,id=${CACHE_ID}-build,target=/workspace/build,uid=10001,gid=10001,sharing=private \
    --mount=type=cache,id=${CACHE_ID}-gradle,target=/workspace/.gradle,uid=10001,gid=10001,sharing=private \
    --mount=type=cache,id=${CACHE_ID}-kotlin,target=/workspace/.kotlin,uid=10001,gid=10001,sharing=private \
    --mount=type=bind,from=exercise,source=cases.yaml,target=/opt/learn/cases.yaml \
    learn-build && learn-check-cases /opt/learn/cases.yaml
