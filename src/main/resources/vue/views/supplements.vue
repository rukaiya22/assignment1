<template id="supplements">
  <app-layout/>
  <div>
    <h2 class="row">Supplement</h2>
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
            Supplement Information
          </div>
          <div class="card-body row" >
            <div class="card-header" v-show="id!=null"><i>Updating Supplement Information with ID {{id}}</i></div>
            <div class="col-md-3"><label class="col-form-label">Vitamin_d:</label></div>
            <div class="col-md-1"><input  type="number" v-model.number="vitamin_d" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Vitamin_c: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="vitamin_c" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Iron: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="iron" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Calcium:</label></div>
            <div class="col-md-1"><input  type="number" v-model.number="calcium" /></div>
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
          <span v-if="userId!=null"> No Supplements for user id {{userId}}. Please add.</span>
          <span v-else>Please click view button for a user.</span>
        </div>
        <div class="row" v-show="showDetails">
          <div class="row row-header2">
            <!-- <div class="col-md-1">Id</div> -->
            <div class="col-md-2">Vitamin_d</div>
            <div class="col-md-2">Vitamin_c</div>
            <div class="col-md-2">Iron</div>
            <div class="col-md-3">Calcium</div>
            <div class="col-md-3">Action</div>
          </div>
          <div class="row row-detail2" v-for="detail in details" :key="detail.id">
            <!-- <div class="col-md-1">{{track.id}}</div> -->
            <div class="col-md-2">{{detail.vitamin_d}} mg</div>
            <div class="col-md-2">{{detail.vitamin_c}} mg</div>
            <div class="col-md-2">{{detail.iron}} mg</div>
            <div class="col-md-3">{{detail.calcium}} mg</div>
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
app.component("supplements", {
  template: "#supplements",
  data: () => ({
    users: [],
    details:[],
    showDetails: false,
    userId: null,
    id: null,
    vitamin_d: null,
    vitamin_c: null,
    iron: null,
    calcium: null,
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
      axios.get("/api/supplements/" + userId)
          .then(res => {
            this.details = res.data;
            this.showDetails = true;
            this.userId = userId;
          })
          .catch(() => {
            // alert("Error while fetching supplements");
            this.showDetails = false;
            this.userId = userId;
          });
    },
    deleteDetail(id) {
      fetch("/api/supplements/" + id, {
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
      if (this.vitamin_d == null || this.vitamin_c == null || this.iron == null || this.calcium == null ||
          this.vitamin_d === "" || this.vitamin_c === "" || this.iron === "" || this.calcium === "") {
        alert("Vitamin_d, Vitamin_c, Iron and Calcium cannot be black");
        return false;
      }

      if (this.vitamin_d < 0 || this.vitamin_c < 0 || this.iron < 0 || this.calcium < 0) {
        alert("Vitamin_d, Vitamin_c, Iron and Calcium cannot be negative. \n Minimum value 0.0");
        return false;
      }

      return true;
    },
    addDetail() {
      if (!this.isInputValid()) return;
      if(this.id == null) {
        fetch("/api/supplements", {
          method: "POST",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ userId: this.userId, vitamin_d: this.vitamin_d, iron:
            this.iron, vitamin_c: this.vitamin_c, calcium:this.calcium }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the supplement could not be saved.");
        });
      } else {
        fetch("/api/supplements", {
          method: "PUT",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ id:this.id, userId: this.userId, vitamin_d: this.vitamin_d,
            iron: this.iron, vitamin_c: this.vitamin_c, calcium: this.calcium }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the supplement could not be saved.");
        });
      }


    },
    updateDetail: function (detail) {
      this.id = detail.id;
      this.vitamin_d = detail.vitamin_d;
      this.vitamin_c = detail.vitamin_c;
      this.iron = detail.iron;
      this.calcium = detail.calcium
    },
    selectInsertMode: function () {
      this.vitamin_d = null;
      this.vitamin_c = null;
      this.iron = null;
      this.calcium = null;
      this.id = null;
    }
  }
});
</script>

