<template>
<i :class="icon" :style="{color : iconColor, fontSize : size}"
   style="cursor: pointer"
   @mouseover="iconColor = enterColor"
   @mouseleave="iconColor = defaultColor"
   @click="onClick"
></i>
</template>

<script>
export default {
  name: 'IconButton',
  props: {
    icon: {
      type: String,
      default: 'el-icon-search'
    },
    size: {
      type: String,
      default: '30px'
    },
    defaultColor: {
      type: String,
      default: '#606266'
    },
    enterColor: {
      type: String,
      default: '#409EFF'
    }
  },
  data () {
    return {
      iconColor: this.defaultColor
    }
  },
  methods: {
    onClick () {
      this.$emit('click')
    }
  }
}

</script>
