<template>
  <el-carousel :interval="4000" type="card" height="400px">
    <el-carousel-item v-for="item in doorImageArr" :key="item">
      <img :src="item" style="width:100%" />
    </el-carousel-item>
  </el-carousel>
</template>

<script>
export default {
  data() {
    return {
      doorImageArr: [
        'static/images/blue.png',
        'static/images/pink.png',
        'static/images/red.png',
        'static/images/yellow.png'
      ]
    }
  },
  methods: {
    toLogin() {
      this.$router.push('/user/login')
    },
    // 游客模式去往首页
    toHome() {
      this.$router.push({ path: '/home' })
    }
  }
}
</script>
