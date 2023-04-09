<template>
<div class="icon-button"
    @mouseover="iconColor = enterColor"
    @mouseleave="iconColor = defaultColor"
    @click="onClick"
    :style="{color : iconColor, flexDirection: flexDirection}"
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
      default: 'right'
    }
  },
  data () {
    return {
      iconColor: this.defaultColor
    }
  },
  computed: {
    flexDirection () {
      switch (this.attachPosition) {
        case 'left':
          return 'row-reverse'
        case 'right':
          return 'row'
        case 'top':
          return 'column-reverse'
        case 'bottom':
          return 'column'
      }
    }
  },
  methods: {
    onClick () {
      this.$emit('click')
    }
  }
}

</script>

<style scoped>
.icon-button {
    cursor: pointer;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

</style>
