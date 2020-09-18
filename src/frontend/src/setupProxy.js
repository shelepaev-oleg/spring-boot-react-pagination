const proxy = require('http-proxy-middleware');
const morgan = require('morgan');

const proxy = require('http-proxy-middleware');

// Данный Proxy не заработал, заработал devServer (см. webpack.common.config.js)
// module.exports = function (app) {
//     app.use(
//         '/api/*',
//         proxy({
//             target: 'http://localhost:8080',
//             changeOrigin: true,
//         })
//     );
//     app.use(morgan('combined'));
// };