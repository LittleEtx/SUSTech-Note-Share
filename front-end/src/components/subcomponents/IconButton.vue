<template>
<div style="cursor: pointer;"
    @mouseover="iconColor = enterColor"
    @mouseleave="iconColor = defaultColor"
    @click="onClick"
    :style="{color : iconColor}"
>
    <i :class="icon" :style="{fontSize: size}"></i>
    <slot></slot>
</div>

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
      default: '20px'
    },
    defaultColor: {
      type: String,
      default: '#606266'
    },
    enterColor: {
      type: String,
      default: '#409EFF'
    },
    attachPosition: {
      type: String,
      range: ['left', 'right', 'top', 'bottom'],
      default: 'left'
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
