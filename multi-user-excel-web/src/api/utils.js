
import CryptoJS from 'crypto-js'
//求Array1中Array2没有的元素集合
export function minusArray (Array1,Array2) {
    return  Array1.filter(function (v) {
        return Array2.indexOf(v) === -1
      })
  }
//邮箱合法性判断
  export function emailVaildCheck (data) {
    if (data !== '') { 
      var reg=/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.com+)+$/
      // var reg=/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
      if(!reg.test(data))
        return false
     
    }
    return true
  }
  //名字合法性判断,只能包含a-z，A-Z，0-9，_和中文
  export function userNameVaildCheck (data) {
    if (data !== '') { 
      var reg=/^[a-zA-Z0-9_\u4e00-\u9fa5]+$/
      if(!reg.test(data))
        return false
     
    }
    return true
  }
  //密码合法性判断,只能包含a-z，A-Z，0-9
  export function passwordVaildCheck (data) {
    if (data !== '') { 
      var reg=/^[a-zA-Z0-9]+$/
      if(!reg.test(data))
        return false
     
    }
    return true
  }
  //生成n位随机码
  export function guid (n) {
    var s = []
    for (let index = 0; index < n/4; index++) {
      s[index] = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1)

    }
    return s.join('')
  }
  //生成随机颜色
  export function getColor() {
    return '#' + Math.floor(Math.random() * 0xffffff).toString(16)
  }
  //偏移量
  const iv =CryptoJS.enc.Utf8.parse(process.env.AES_IV)
  
  //加密
  export function encrypt(data){
    console.log('加密前:'+data)
    let dateStr = getDateString()
   //密钥
   let realKey=dateStr + dateStr.split('').reverse().join('')
   
   var encrypted = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(data), CryptoJS.enc.Utf8.parse(realKey), {
     iv: iv,
     mode: CryptoJS.mode.CBC,
     padding: CryptoJS.pad.ZeroPadding
   })

   console.log('加密后:'+CryptoJS.enc.Base64.stringify(encrypted.ciphertext))
   return CryptoJS.enc.Base64.stringify(encrypted.ciphertext)
  }

  export function getDateString() {
    let date = new Date()
    let year=date.getFullYear()
    let month=date.getMonth()+1>9?date.getMonth()+1:'0'+(date.getMonth()+1)
    let day=date.getDate()>9?date.getDate():'0'+date.getDate()
    return year+month+day
  }