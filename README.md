# Movie Fun!

Smoke Tests require server running on port 8080 by default.

## Build JAR ignoring tests

```bash
$ ./gradlew clean build -xtest
```

## Run Smoke Tests against specific URL

```bash
$ MOVIE_FUN_URL=http://moviefun.example.com ./gradlew test
```



export MINIO_ACCESS_KEY=U66UHZA0M5Y3AGLHEJQC
export MINIO_SECRET_ACCESS_KEY=i/UA0uHjejqM1SlyETY8rzdA5NorPldX66DJUTRb
export S3_ENDPOINTURL=http://127.0.0.1:9000
export VCAP_SERVICES="{\"aws-s3\": [{\"credentials\": {\"access_key_id\": \"U66UHZA0M5Y3AGLHEJQC\", \"bucket\": \"moviefun\", \"secret_access_key\": \"i/UA0uHjejqM1SlyETY8rzdA5NorPldX66DJUTRb\"}, \"label\": \"aws-s3\", \"name\": \"moviefun-s3\"}]}"
export APPLICATION_OAUTH_ENABLED=false

export EUREKA_CLIENT_ENABLED=false
export RIBBON_EUREKA_ENABLED=false

export ALBUM_SERVICE_RIBBON_LISTOFSERVERS=http://localhost:8081
export MOVIE_SERVICE_RIBBON_LISTOFSERVERS=http://localhost:8082

export MOVIES_PAGESIZE=6

export ALBUMS_URL=http://localhost:8081/albums
export MOVIES_URL=http://localhost:8082/movies
