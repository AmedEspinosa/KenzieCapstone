const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    calendarPage: path.resolve(__dirname, 'src', 'pages', 'calendarPage.js'),
    landingPage: path.resolve(__dirname, 'src', 'pages', 'landingPage.js'),
    searchPage: path.resolve(__dirname, 'src', 'pages', 'searchPage.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    openPage: 'http://localhost:8080/home_page.html',
    // diableHostChecks, otherwise we get an error about headers and the page won't render
    disableHostCheck: true,
    contentBase: 'packaging_additional_published_artifacts',
    // overlay shows a full-screen overlay in the browser when there are compiler errors or warnings
    overlay: true,
    proxy: [
      {
        context: [
          '/event'
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/home_page.html',
      filename: 'home_page.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/calendar.html',
      filename: 'calendar.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/create_account_page.html',
      filename: 'create_account_page.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/forgot_password.html',
      filename: 'forgot_password.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/login_page.html',
      filename: 'login_page.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/search_and_login_landing_page.html',
      filename: 'search_and_login_landing_page.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/search_page.html',
      filename: 'search_page.html',
      inject: false
    }),

    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        },
        {
          from: path.resolve('src/images'),
          to: path.resolve("dist/images")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}