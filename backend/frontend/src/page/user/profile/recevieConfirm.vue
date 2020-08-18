<template>
  <div class="update-request">
    <select v-model="category" name id>
      <option value="all">전체</option>
      <option value="read">읽음</option>
      <option value="unread">읽지않음</option>
      <option value="middlestate">고려 중</option>
    </select>
    <div class="table">
      <tr>
        <th>No.</th>
        <th>수정 요청 글</th>
        <th>요청한 유저</th>
        <th>읽은 상태</th>
        <th>처리 상태</th>
        <th>처리 결과</th>
      </tr>
      <tr
        v-for="(recevieConfirm,index) in recevieConfirmBystatus"
        :key="index"
        data-toggle="modal"
        :data-target="'#receiveconfrim'+recevieConfirm.cid"
      >
        <td @click="getConfirmDetail">{{index+1}}</td>
        <td @click="getConfirmDetail">{{recevieConfirm.posttitle}}</td>
        <td @click="getConfirmDetail">{{recevieConfirm.cwriter}}</td>
        <td @click="getConfirmDetail">{{recevieConfirm.read}}</td>
        <td @click="getConfirmDetail">{{recevieConfirm.state}}</td>
        <td @click="getConfirmDetail">{{recevieConfirm.nextState}}</td>
        <div
          class="modal fade"
          :id="'receiveconfrim'+recevieConfirm.cid"
          tabindex="-1"
          role="dialog"
          aria-labelledby="exampleModalLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">...</div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
              </div>
            </div>
          </div>
        </div>
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
    recevieConfirmBystatus() {
      if (this.category === "read") {
        return this.recevieConfirmlist.filter((item) => {
          return item.ischecked;
        });
      } else if (this.category === "unread") {
        return this.recevieConfirmlist.filter((item) => {
          return !item.ischecked;
        });
      } else if (this.category === "middlestate") {
        return this.recevieConfirmlist.filter((item) => {
          return item.willmodify === 0;
        });
      } else {
        return this.recevieConfirmlist;
      }
    },
  },
  data() {
    return {
      constants,
      category: "all",
      recevieConfirmlist: [],
    };
  },
  methods: {
    getConfirmDetail() {
      console.log("실행되었어요");
    },
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
          this.recevieConfirmlist = res.data;
          this.recevieConfirmlist = this.recevieConfirmlist.map((item) => {
            if (item.ischecked === false) {
              item.read = "읽지않음";
            } else {
              item.read = "읽음";
            }
            if (item.willmodify === -1) {
              item.state = "수정 거절";
            } else if (item.willmodify === 0) {
              item.state = "고려중";
            } else {
              item.state = "수정 승인";
            }
            if (item.ismodified === false) {
              item.nextState = "수정 중";
            } else {
              item.nextState = "수정완료";
            }
            return item;
          });
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
  },
  created() {
    this.getReceiveMrcomment();
  },
};
</script>

<style></style>
