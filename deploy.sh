#! /bin/bash
# shellcheck disable=SC2164
cd ./client
flutter build web --release
rm -rf ../server/src/main/resources/static/*
cp -r build/web/* ../server/src/main/resources/static

cd ../server
./mvnw clean package -DskipTests=true

scp -i "~/.ssh/intel.pem" ./target/server-1.0.0.jar ubuntu@ec2-54-180-83-202.ap-northeast-2.compute.amazonaws.com:/home/ubuntu/intel