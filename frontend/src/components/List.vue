<template>
    <div id="list">
         <img src="http://localhost:9090/download/1" alt="안되요 ㅠㅠ">
        <div v-for="(file, index) in files" :key="index">
         <a href="#" @click="test(1, file['originName'])">다운로드</a>
         {{  file['originName'] }}
        </div>
    </div>
</template>
<script>
import axios from 'axios'

export default {
  name: 'list',
  data () {
    return {
      files: null,
      download: null
    }
  },
  created () {
    axios.get('http://localhost:9090/file/list')
      .then((res) => {
        let obj2 = JSON.stringify(res.data)
        let obj3 = JSON.parse(obj2)
        this.files = obj3
        console.log(this.files)
      })
  },
  methods: {
    test (index, name) {
      console.log(index, 'click')
      console.log(name)
      axios({
        url: 'http://localhost:9090/download/1',
        method: 'GET',
        responseType: 'blob'
      }).then((res) => {
        console.log(res)
        let fileURL = window.URL.createObjectURL(new Blob([res.data], {type: res.headers['content-type']}))
        let fileLink = document.createElement('a')

        fileLink.href = fileURL
        fileLink.setAttribute('download', name)
        document.body.appendChild(fileLink)

        fileLink.click()
      })
      // axios.get('http://localhost:9090/download/' + 1, {
      // })
      // .then((res) => {
      //   console.log('res: ', res)
      //   // eslint-disable-next-line no-unused-vars
      //   function replaceAll (str, searchStr, replaceStr) {
      //     return str.split(searchStr).join(replaceStr)
      //   }
      //   console.log('res.headers: ', res.headers)
      //   console.log('res.data: ', res.data)
      //   const url = window.URL.createObjectURL(new Blob([res.data], {type: res.headers['content-type']}))
      //   const link = document.createElement('a')
      //   link.href = url
      //   const filename = res.data['savedName']
      //   link.setAttribute('download', filename)
      //   document.body.appendChild(link)
      //   link.click()
      // .catch((err) => { console.log(err) })
    }
  }

}
</script>
