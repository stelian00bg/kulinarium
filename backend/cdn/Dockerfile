FROM golang:alpine AS builder

WORKDIR /cdn

COPY . .

RUN go build -o ./cdn

FROM alpine AS cdn

WORKDIR /cdn

EXPOSE 8081

COPY --from=builder /cdn .

RUN apk update && apk upgrade && apk add curl && apk add bash

ENTRYPOINT [ "./cdn" ]