<script setup lang="ts">
import {ref} from "vue";
interface Props {
  url?: string
}
const props: Props = withDefaults(defineProps<Props>(), {
  url: this.$store.state.userInfo.avatar
})
const showDefault = ref(false)

</script>
<template>
<el-avatar>
  <template v-if="url">
    <img :src="url" alt="avatar">
  </template>
  <template v-else>
    <el-icon name="el-icon-user-solid"></el-icon>
  </template>
</el-avatar>
</template>

<style scoped>

</style>