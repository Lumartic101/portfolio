module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    'plugin:vue/vue3-essential',
    "@vue/typescript/recommended",
    'eslint:recommended',
  ],
  plugins: [
    'vue',
    '@typescript-eslint',
  ],
  parserOptions: {
    parser: '@typescript-eslint/parser',
  },
  rules: {
    'semi': ['error', 'always'],
    'quotes': ['error', 'single'],
    'indent': ['error', 2],
    'no-trailing-spaces': ['error'],
    'comma-spacing': ['error', { 'before': false, 'after': true }],
    'space-before-blocks': ['error', 'always'],
    "no-unused-vars": ["error", { "vars": "all", "args": "none", "varsIgnorePattern": "^_$|^e$|^id$|^event" }]
  },
}