<template>
  <div>
    <h1>error!{{msg}}</h1>
    <div  style=" background-color: green;height:800px"></div>
  </div>
</template>
<script>
export default {
  data () {
    return {
      msg: this.$route.query.data
    }
  }
}
</script>
