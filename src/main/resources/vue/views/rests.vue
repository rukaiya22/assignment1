<template id="rests">
  <app-layout/>
  <div>
    <h2 class="row">Rest</h2>
    <div class="row"><br /></div>
    <div class="row">
      <div class="col-md-5">
        <div class="row"><br /></div>
        <div class="row row-header">
          <div class="col-md-1">Id</div>
          <div class="col-md-3">Name</div>
          <div class="col-md-5">Email</div>
          <div class="col-md-3">Action</div>
        </div>
        <div class="row row-detail" v-for="user in users" :key="user.id">
          <div class="col-md-1">{{user.id}}</div>
          <div class="col-md-3">{{user.name}}</div>
          <div class="col-md-5">{{user.email}}</div>
          <div class="col-md-3">
            <button rel="tooltip" title="View" @click="fetchDetails(`${user.id}`)">
              <i class="fas fa-eye"  aria-hidden="true"></i></button>
          </div>
        </div>
      </div>
      <div class="col-md-1">
        <br />
      </div>
      <div class="col-md-6">
        <div class="row row-header" v-show="users.length !== 0 && userId!=null"><b>User Id: {{userId}}</b></div>
        <div class="row"><br /></div>
        <div class="card bg-light mb-3"  v-show="users.length !== 0 && userId!=null">
          <div class="card-header">
            Rest Information
          </div>
          <div class="card-body row" >
            <div class="card-header" v-show="id!=null"><i>Updating Rest Information with ID {{id}}</i></div>
            <div class="col-md-3"><label class="col-form-label">Sleep:</label></div>
            <div class="col-md-1"><input  type="number" v-model.number="sleep" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Power_nap: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="power_nap" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Meditation: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="meditation" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-4"></div>
            <div class="col-md-8">
              <button rel="tooltip" title="Save" @click="addDetail()">
                <i class="fas fa-save"  aria-hidden="true"></i></button>
              &nbsp;
              <button v-show="id!=null" rel="tooltip" title="Insert mode" @click="selectInsertMode()">
                <i class="fas fa-add"  aria-hidden="true"></i></button>
            </div>
          </div>
        </div>
        <div class="row"><br /></div>
        <div class="row" v-show="!showDetails">
          <span v-if="userId!=null"> No Rests for user id {{userId}}. Please add.</span>
          <span v-else>Please click view button for a user.</span>
        </div>
        <div class="row" v-show="showDetails">
          <div class="row row-header2">
            <!-- <div class="col-md-1">Id</div> -->
            <div class="col-md-3">Sleep</div>
            <div class="col-md-3">Power_nap</div>
            <div class="col-md-3">Meditation</div>
            <div class="col-md-2">Action</div>
          </div>
          <div class="row row-detail2" v-for="detail in details" :key="detail.id">
            <!-- <div class="col-md-1">{{track.id}}</div> -->
            <div class="col-md-3">{{detail.sleep}} hours</div>
            <div class="col-md-3">{{detail.power_nap}} hours</div>
            <div class="col-md-3">{{detail.meditation}} hours</div>
            <div class="col-md-3">
              <button rel="tooltip" title="Edit" @click="updateDetail(detail)">
                <i class="fas fa-pencil"  aria-hidden="true"></i></button> &nbsp;
              <button rel="tooltip" title="Delete" @click="deleteDetail(`${detail.id}`)">
                <i class="fas fa-trash"  aria-hidden="true"></i></button>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
app.component("rests", {
  template: "#rests",
  data: () => ({
    users: [],
    details:[],
    showDetails: false,
    userId: null,
    id: null,
    sleep: null,
    power_nap: null,
    meditation: null,
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    fetchDetails(userId) {
      axios.get("/api/rests/" + userId)
          .then(res => {
            this.details = res.data;
            this.showDetails = true;
            this.userId = userId;
          })
          .catch(() => {
            // alert("Error while fetching rests");
            this.showDetails = false;
            this.userId = userId;
          });
    },
    deleteDetail(id) {
      fetch("/api/rests/" + id, {
        method: "DELETE",
        cache: "no-store",
      }).then((response) => {
        if (response.status !== 404) {
          alert("The item is deleted");
          this.fetchDetails(this.userId);
        }
      }).catch((err) => {
        console.log("there is an error");
      });
    },
    isInputValid: function () {
      if (this.sleep == null || this.power_nap == null || this.meditation == null ||
          this.sleep === "" || this.power_nap === "" || this.meditation === "") {
        alert("Sleep, Power_nap and Meditation cannot be black");
        return false;
      }

      if (this.sleep < 0 || this.power_nap < 0 || this.meditation <0 ) {
        alert("Sleep, Power_nap and Meditation cannot be negative. \n Minimum value 0.0");
        return false;
      }

      return true;
    },
    addDetail() {
      if (!this.isInputValid()) return;
      if(this.id == null) {
        fetch("/api/rests", {
          method: "POST",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ userId: this.userId, sleep: this.sleep, meditation:
            this.meditation, power_nap: this.power_nap }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the rest could not be saved.");
        });
      } else {
        fetch("/api/rests", {
          method: "PUT",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ id:this.id, userId: this.userId, sleep: this.sleep,
            meditation: this.meditation, power_nap: this.power_nap }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the rest could not be saved.");
        });
      }


    },
    updateDetail: function (detail) {
      this.id = detail.id;
      this.sleep = detail.sleep;
      this.power_nap = detail.power_nap;
      this.meditation = detail.meditation;
    },
    selectInsertMode: function () {
      this.sleep = null;
      this.power_nap = null;
      this.meditation = null;
      this.id = null;
    }
  }
});
</script>

