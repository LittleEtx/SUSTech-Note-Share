<template>

</template>

<script>
export default {
  name: 'FoldingText',
  props: {
    text: {
      type: String,
      require: true
    },
    defaultHeight: {
      type: Number,
      default: 200
    }
  }
}
</script>

<style scoped>

</style>
