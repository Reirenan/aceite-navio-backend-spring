## 🚀 Executando o Projeto com Docker

```bash
# 1. Inicie o Docker Desktop

# 2. Construa a imagem Docker
docker build -t docker-demo-app-two .

# 3. Execute o container
docker run -p 9090:8080 docker-demo-app-two

# A aplicação estará disponível em: http://localhost:9090
