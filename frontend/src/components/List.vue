<template>
    <div id="list">
        <!--일반적으로 이렇게 해서 클릭하면 모든 확장자 다운로드 가능 -->
        <!-- <a href="http://localhost:9090/download/2">hwp 체크</a> -->
        <div v-for="(file, index) in files" :key="index">
          {{ file }} {{ index }}
         <!-- <img src="http://localhost:9090/download/" + {{index}}> -->
         <!-- <a href="#" @click="test(index, file['originName'])">{{ file['originName'] }}{{ file['ext'] }}</a> -->
         <a href="#" @click.prevent="downloadTest(index, file['originName'])">{{ file['originName'] }}{{ file['ext'] }}</a>
        </div>
    </div>
</template>
<script>
import axios from 'axios'
const DEV_URL = 'http://localhost:9090'

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
      // axios({
      //   url: this.url + index,
      //   method: 'GET',
      //   responseType: 'blob'
      // }).then((res) => {
      //   console.log(res)
      //   console.log(res.headers['content-type'])
      //   let fileURL = window.URL.createObjectURL(new Blob([res.data], {type: res.headers['content-type']}))
      //   let fileLink = document.createElement('a')
      //   console.log('fileURL: ', fileURL)
      //   fileLink.href = fileURL
      //   fileLink.setAttribute('download', name)
      //   document.body.appendChild(fileLink)

      //   fileLink.click()
      // })

      axios.get(DEV_URL + '/download/' + index,
        {responseType: 'blob'})
        .then((res) => {
          function getFileName (content) {
            let fileName = content.split(';').filter((ele) => {
              return ele.indexOf('filename') > -1
            })
              .map((ele) => {
                return ele
                  .replace(/"/g, '')
                  .split('=')[1]
              })
            return fileName[0] ? fileName[0] : null
          }
          // console.log(res)
          // const url = window.URL.createObjectURL(new Blob([res.data], {type: res.headers['content-type']}))
          // const link = document.createElement('a')
          // link.href = url
          // link.setAttribute('download', name)
          // document.body.appendChild(link)
          // link.click()
          let blob = new Blob([res.data], {type: res.headers['content-type']})
          let fileName = getFileName(res.headers['content-disposition'])
          fileName = decodeURI(fileName)

          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.target = '_self'
          if (fileName) link.download = fileName
          link.click()
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
    },
    downloadTest (index, name) {
      axios.get(DEV_URL + '/v2/download/' + index, {responseType: 'blob'})
        .then((res) => {
          console.log(res)
          console.log(this.files)
          const url = window.URL.createObjectURL(new Blob([res.data], {type: this.files[index]['contentType']}))
          const link = document.createElement('a')
          link.href = url
          if (this.files[index]['contentType'].includes('octet')) {
            link.setAttribute('download', name + this.files[index]['ext'])
          } else {
            link.setAttribute('download', name)
          }
          document.body.appendChild(link)
          link.click()
        })
    }
  }

}
</script>
