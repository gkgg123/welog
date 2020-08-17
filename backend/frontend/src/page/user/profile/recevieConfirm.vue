<template>
  <div class="update-request">
    <div class="table">
      <tr>
        <th>No.</th>
        <th>수정 요청 글</th>
        <th>요청한 유저</th>
        <th>요청일</th>
        <th>처리 상태</th>
      </tr>
      <tr>
        <td>1</td>
        <td>welog 잘만들었다</td>
        <td>김영환</td>
        <td>2020-08-15</td>
        <td>reject</td>
      </tr>
      <tr>
        <td>1</td>
        <td>welog 잘만들었다</td>
        <td>김영환</td>
        <td>2020-08-15</td>
        <td>reject</td>
      </tr>
      <tr>
        <td>1</td>
        <td>welog 잘만들었다</td>
        <td>김영환</td>
        <td>2020-08-15</td>
        <td>reject</td>
      </tr>
    </div>
    <button class="btn btn-primary" @click="getReceiveMrcomment">요청</button>
  </div>
</template>

<script>
import axios from "axios";
import constants from "@/lib/constants.js";
import { mapState } from "vuex";
export default {
  name: "recevieConfirm",
  computed: {
    ...mapState(["authToken"]),
  },
  data() {
    return {
      constants,
    };
  },
  methods: {
    getReceiveMrcomment() {
      const totalData = {
        Authorization: this.authToken,
      };
      axios
        .get(constants.baseUrl + "modfrequest/author", {
          headers: {
            Authorization: this.authToken,
          },
        })
        .then((res) => {
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
  },
};
</script>

<style></style>
