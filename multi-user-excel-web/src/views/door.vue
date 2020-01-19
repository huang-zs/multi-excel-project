<template>
  <el-row>
    <el-col :span="20">
      <el-carousel :interval="4000" type="card" height="400px">
        <el-carousel-item>
          <img src="static/images/blue.png" style="width:100%" />
        </el-carousel-item>
        <el-carousel-item v-for="item in doorImageArr" :key="item">
          <h1>{{item}}</h1>
          <img :src="item" style="width:100%" />
          <!-- <img src="../assets/blue.png" style="width:100%" /> -->
        </el-carousel-item>
      </el-carousel>
    </el-col>
    <el-col :span="4">
      <div>
        <el-button type @click="toHome">游客模式</el-button>
      </div>
      <div>
        <el-button type @click="toLogin">用户模式</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  data () {
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
    toLogin () {
      this.$router.push('/user/login')
    },
    // 游客模式去往首页
    toHome () {
      this.$router.push({path: '/home'})
    }
  }
}
</script>
