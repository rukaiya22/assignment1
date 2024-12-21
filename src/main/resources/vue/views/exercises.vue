<template id="exercises">
  <app-layout/>
  <div>
    <h2 class="row">Exercise</h2>
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
            Exercise Information
          </div>
          <div class="card-body row" >
            <div class="card-header" v-show="id!=null"><i>Updating Exercise Information with ID {{id}}</i></div>
            <div class="col-md-3"><label class="col-form-label">Running:</label></div>
            <div class="col-md-1"><input  type="number" v-model.number="running" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Swimming: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="swimming" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Cycling: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="cycling" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Equipment_based:</label></div>
            <div class="col-md-1"><input  type="number" v-model.number="equipment_based" /></div>
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
          <span v-if="userId!=null"> No Exercises for user id {{userId}}. Please add.</span>
          <span v-else>Please click view button for a user.</span>
        </div>
        <div class="row" v-show="showDetails">
          <div class="row row-header2">
            <!-- <div class="col-md-1">Id</div> -->
            <div class="col-md-2">Running</div>
            <div class="col-md-2">Swimming</div>
            <div class="col-md-2">Cycling</div>
            <div class="col-md-3">Equipment_based</div>
            <div class="col-md-3">Action</div>
          </div>
          <div class="row row-detail2" v-for="detail in details" :key="detail.id">
            <!-- <div class="col-md-1">{{track.id}}</div> -->
            <div class="col-md-2">{{detail.running}} hours</div>
            <div class="col-md-2">{{detail.swimming}} hours</div>
            <div class="col-md-2">{{detail.cycling}} hours</div>
            <div class="col-md-3">{{detail.equipment_based}} hours</div>
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
app.component("exercises", {
  template: "#exercises",
  data: () => ({
    users: [],
    details: [],
    showDetails: false,
    userId: null,
    id: null,
    running: null,
    swimming: null,
    cycling: null,
    equipment_based: null,
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
      axios.get("/api/exercises/" + userId)
          .then(res => {
            this.details = res.data;
            this.showDetails = true;
            this.userId = userId;
          })
          .catch(() => {
            // alert("Error while fetching exercises");
            this.showDetails = false;
            this.userId = userId;
          });
    },
    deleteDetail(id) {
      fetch("/api/exercises/" + id, {
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
      if (this.running == null || this.swimming == null || this.cycling == null || this.equipment_based == null ||
          this.running === "" || this.swimming === "" || this.cycling === "" || this.equipment_based === "") {
        alert("Running, Swimming, Cycling and Equipment_based cannot be black");
        return false;
      }

      if (this.running < 0 || this.swimming < 0 || this.cycling < 0 || this.equipment_based < 0) {
        alert("Running, Swimming, Cycling and Equipment_based cannot be negative. \n Minimum value 0.0");
        return false;
      }

      return true;
    },
    addDetail() {
      if (!this.isInputValid()) return;
      if (this.id == null) {
        fetch("/api/exercises", {
          method: "POST",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            userId: this.userId, running: this.running, cycling:
            this.cycling, swimming: this.swimming, equipment_based: this.equipment_based
          }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the exercise could not be saved.");
        });
      } else {
        fetch("/api/exercises", {
          method: "PUT",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            id: this.id, userId: this.userId, running: this.running,
            cycling: this.cycling, swimming: this.swimming, equipment_based: this.equipment_based
          }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the exercise could not be saved.");
        });
      }


    },
    updateDetail: function (detail) {
      this.id = detail.id;
      this.running = detail.running;
      this.swimming = detail.swimming;
      this.cycling = detail.cycling;
      this.equipment_based = detail.equipment_based
    },
    selectInsertMode: function () {
      this.running = null;
      this.swimming = null;
      this.cycling = null;
      this.equipment_based = null;
      this.id = null;
    }
  }
});
</script>

