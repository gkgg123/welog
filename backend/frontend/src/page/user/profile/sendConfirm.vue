<template>
  <div class="update-request">
    <div class="select-bar">
      <select v-model="category" name id>
        <option value="all">전체</option>
        <option value="read">읽음</option>
        <option value="unread">읽지않음</option>
        <option value="middlestate">대기 중</option>
      </select>
    </div>
    <div class="table">
      <tr>
        <th>No.</th>
        <th>수정 요청 글</th>
        <th>받은 사람</th>
        <th>읽음 상태</th>
        <th>처리 상태</th>
        <th>처리 결과</th>
      </tr>
      <tr v-for="(recevieConfirm,index) in recevieConfirmBystatus" :key="recevieConfirm.cid">
        <td>{{index+1}}</td>
        <td
          class="post-title"
          data-toggle="modal"
          :data-target="'#receiveconfirm'+recevieConfirm.cid"
        >{{recevieConfirm.posttitle}}</td>
        <td>{{recevieConfirm.pwriter}}</td>
        <td>{{recevieConfirm.read}}</td>
        <td>{{recevieConfirm.state}}</td>
        <td>{{recevieConfirm.nextState}}</td>
        <div
          class="modal fade"
          :id="'receiveconfirm'+recevieConfirm.cid"
          tabindex="-1"
          role="dialog"
          aria-labelledby="exampleModalLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <p class="modal-title" id="exampleModalLabel">글 제목 : {{recevieConfirm.posttitle}}</p>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <div class="before-update">
                  <div>수정 요청 부분</div>
                  <p>- {{recevieConfirm.rstring}}</p>
                </div>

                <div class="after-update">
                  <div>수정 요청 내역</div>
                  <p>- {{recevieConfirm.rcomment}}</p>
                </div>
                <div class="request-radio">
                  <label class="box-radio-input">
                    <input
                      type="radio"
                      name="cp_item"
                      v-model="recevieConfirm.willmodify"
                      value="1"
                      checked="checked"
                    />
                    <span>승인</span>
                  </label>
                  <label class="box-radio-input">
                    <input
                      type="radio"
                      name="cp_item"
                      v-model="recevieConfirm.willmodify"
                      value="-1"
                    />
                    <span>거절</span>
                  </label>
                </div>
                <div v-if="recevieConfirm.willmodify == 1">
                  <div>처리 상태</div>
                  <div class="request-radio">
                    <label class="box-radio-input">
                      <input
                        type="checkbox"
                        name="cp_item"
                        v-model="recevieConfirm.ismodified"
                        checked="checked"
                      />
                      <span>수정완료</span>
                    </label>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button">완료</button>
                <button type="button" data-dismiss="modal">취소</button>
              </div>
            </div>
          </div>
        </div>
      </tr>
    </div>
    <div class="table-under">
      <button>요청</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import constants from "@/lib/constants.js";
export default {
  name: "sendConfirm",
  computed: {
    ...mapState(["authToken"]),
    recevieConfirmBystatus() {
      this.recevieConfirmlist = this.recevieConfirmlist.map((item) => {
        if (item.ischecked === false) {
          item.read = "읽지 않음";
        } else {
          item.read = "읽음";
        }
        if (item.willmodify == -1) {
          item.state = "수정 거절";
        } else if (item.willmodify == 0) {
          item.state = "대기 中";
        } else {
          item.state = "수정 승인";
        }
        if (item.ismodified === false) {
          item.nextState = "수정 中";
        } else {
          item.nextState = "수정완료";
        }
        return item;
      });

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
          return item.willmodify == 0;
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
    getReceiveMrcomment() {
      const totalData = {
        Authorization: this.authToken,
      };
      axios
        .get(constants.baseUrl + "modfrequest/commentwriter", {
          headers: {
            Authorization: this.authToken,
          },
        })
        .then((res) => {
          this.recevieConfirmlist = res.data;
          this.recevieConfirmlist = this.recevieConfirmlist.map((item) => {
            if (item.ischecked === false) {
              item.read = "읽지 않음";
            } else {
              item.read = "읽음";
            }
            if (item.willmodify == -1) {
              item.state = "수정 거절";
            } else if (item.willmodify == 0) {
              item.state = "대기 中";
            } else {
              item.state = "수정 승인";
            }
            if (item.ismodified === false) {
              item.nextState = "수정 中";
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

<style>
</style>