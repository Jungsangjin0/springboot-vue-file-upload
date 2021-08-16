<template>
  <div class="hello">
    <input type="file" @change="upload" multiple>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      files: null
    }
  },
  methods: {
    upload (e) {
      this.files = e.target.files
      const formData = new FormData()
      formData.append('name', 'Sangin')
      if (this.files && this.files[0]) {
        for (let i = 0; i < this.files.length; i++) {
          formData.append('files', this.files[i])
        }
      }
      axios.post('http://localhost:9090/upload', formData, {
        header: {'Content-type': 'multipart/form-data'}

      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
