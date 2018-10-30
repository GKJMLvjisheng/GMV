module.exports = {
    baseUrl: '/',
    devServer: {
        proxy: {
            '/api': {
                target: 'https://oas.cascv.com/api/v1',
                //target: 'https://dapp.oases.pro/api/v1',
               
                //target: 'http://localhost:8080/api/v1',
                //target: 'https://localhost/api/v1',
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    }
}