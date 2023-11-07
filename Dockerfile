FROM ghcr.io/navikt/baseimages/temurin:21

COPY ./target/familie-baks-infotrygd-feed.jar "app.jar"
