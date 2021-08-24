<template>
    <div>
        HTTP method test
        <button @click="getTest">get Test </button>
        <button @click="postTest">post테스트</button>
        <button @click="patchTest">patch Test</button>
        <button @click="deleteTest">delete Test</button>
    </div>
</template>

<script>
import axios from 'axios'
export default {
  data () {
    return {
      a: {
        'title': 'title',
        'content': 'content'
      }
    }
  },

  methods: {
    getTest () {
      axios.get('http://localhost:9090/getTest', {
      }).then((res) => { console.log(res) })
    },
    postTest () {
      axios.post('http://localhost:9090/postTest', this.a)
    },
    patchTest () {
      axios.patch('http://localhost:9090/patchTest', this.a)
    },
    deleteTest () {
      axios.delete('http://localhost:9090/deleteTest', {
        data: {
          title: 'title',
          content: 'content'
        },
        headers: {'Content-Type': 'application/json;charset=utf8;'}
      })
    }
  }
}
</script>
