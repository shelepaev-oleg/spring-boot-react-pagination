const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
    entry: {
        app: ['./src/index.tsx'],
        vendor: ['react', 'react-dom']
    },
    output: {
        path: path.resolve(__dirname, '../build'),
        filename: '[name].[chunkhash].js',
    },

    devtool: 'source-map',

    module: {
        rules: [
            {
                test: /\.(ts|tsx)$/,
                loader: 'ts-loader'
            },
            { enforce: "pre", test: /\.js$/, loader: "source-map-loader" },
            {
                test: [/.css$|.scss$/],
                use: [MiniCssExtractPlugin.loader, 'css-loader', 'sass-loader', 'postcss-loader'],
            },
            {
                test: /\.(png|jpg|jpeg|gif|svg)$/,
                use: [
                    {
                        loader: 'file-loader',
                        options: {
                            name: '[name].[ext]',
                            outputPath: 'assets/images/',
                        },
                    },
                ],
            },
        ],
    },

    resolve: {
        alias: {
            '@scss': path.resolve(__dirname, '../src/styles/scss'),
            '@css': path.resolve(__dirname, '../src/styles/css'),
            '@img': path.resolve(__dirname, '../src/assets/images'),
            '@': path.resolve(__dirname, '../src'),
        },
        modules: ['node_modules', path.resolve(__dirname, 'src')],
        extensions: ['.ts', '.tsx', '.js'],
    },

    plugins: [
        new HtmlWebpackPlugin({
            title: 'Webpack 4 Starter',
            template: './src/index.html',
            inject: true,
            minify: {
                removeComments: true,
                collapseWhitespace: true,
            },
            favicon: "./src/assets/images/favicon.ico"
        }),
        new MiniCssExtractPlugin({
            filename: 'style.[chunkhash].css',
        }),
        new CopyWebpackPlugin([
            {
                from: './src/assets/images',
                to: 'assets/images',
            },
        ]),
        new CleanWebpackPlugin(),
    ],

    // Proxy
    devServer: {
        proxy: {
            '/api/*': {
                target: 'http://localhost:8080',
                secure: false,
                changeOrigin: true,
                prependPath: false
            }
        },
        historyApiFallback: true,

        // Отключим сообщения, которые не являются ошибками
        stats: 'errors-only',
        clientLogLevel: 'error'
    }
};
