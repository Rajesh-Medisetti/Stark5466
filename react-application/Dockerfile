### STAGE 1: Build ###
FROM node:14.16.0 as build
WORKDIR /usr/src/app
COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile --no-cache --production
COPY ./ /usr/src/app
RUN yarn build

### STAGE 2: Production Environment ###
FROM nginx:alpine
RUN mkdir -p /usr/share/nginx/html
COPY --from=build /usr/src/app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]
