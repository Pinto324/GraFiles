FROM node:20 AS build

RUN  mkdir -p /app
# Configura el directorio de trabajo en el contenedor
WORKDIR /app

# Copia los archivos de Angular al contenedor
COPY package.json /app

# Instala las dependencias y construye el proyecto
RUN npm install

COPY . /app

RUN npm run build --prod

# Usa una imagen de Nginx para servir el contenido estático de Angular
FROM nginx:alpine

# Copia los archivos compilados a la carpeta de Nginx
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Expone el puerto 80
EXPOSE 4200

# Ejecuta Nginx
CMD ["nginx", "-g", "daemon off;"]