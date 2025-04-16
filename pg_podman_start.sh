podman run \
  -it --rm -d \
  --name pgvector \
  -p 5432:5432 \
  -v ./src/main/sql:/data/sql \
  -e POSTGRES_PASSWORD="123" \
   pgvector:0.8.0-pg17

