<template>
  <el-tooltip class="item" effect="dark" :content="content" placement="top">
    <i class="el-icon-question" />
  </el-tooltip>
</template>
<script>
export default {
  name: 'my-tooltip',
  props: ['content']
}
</script>