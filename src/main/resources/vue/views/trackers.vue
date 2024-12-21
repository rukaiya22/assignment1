<template id="trackers">
  <app-layout/>
  <div>
    <h2 class="row">Basic Info</h2>
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
            Basic Information
          </div>
          <div class="card-body row" >
            <div class="card-header" v-show="id!=null"><i>Updating Basic Information with ID {{id}}</i></div>
            <div class="col-md-3"><label class="col-form-label">Calories:</label></div>
            <div class="col-md-1"><input  type="number" v-model.number="calories" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Walking Hours: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="walkHours" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Drinking: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="drinking" /></div>
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
          <span v-if="userId!=null"> No Trackers for user id {{userId}}. Please add.</span>
          <span v-else>Please click view button for a user.</span>
        </div>
        <div class="row" v-show="showDetails">
          <div class="row row-header2">
            <!-- <div class="col-md-1">Id</div> -->
            <div class="col-md-3">Calories</div>
            <div class="col-md-3">Walking</div>
            <div class="col-md-3">Drinking</div>
            <div class="col-md-2">Action</div>
          </div>
          <div class="row row-detail2" v-for="detail in details" :key="detail.id">
            <!-- <div class="col-md-1">{{track.id}}</div> -->
            <div class="col-md-3">{{detail.calories}} K.Cal</div>
            <div class="col-md-3">{{detail.walkHours}} KM</div>
            <div class="col-md-3">{{detail.drinking}} L</div>
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
app.component("trackers", {
  template: "#trackers",
  data: () => ({
    users: [],
    details:[],
    showDetails: false,
    userId: null,
    id: null,
    calories: null,
    walkHours: null,
    drinking: null,
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
      axios.get("/api/trackers/" + userId)
          .then(res => {
            this.details = res.data;
            this.showDetails = true;
            this.userId = userId;
          })
          .catch(() => {
            // alert("Error while fetching trackers");
            this.showDetails = false;
            this.userId = userId;
          });
    },
    deleteDetail(id) {
      fetch("/api/trackers/" + id, {
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
      if (this.calories == null || this.walkHours == null || this.drinking == null ||
          this.calories === "" || this.walkHours === "" || this.drinking === "") {
        alert("Calories, Walking Hours and Drinking cannot be black");
        return false;
      }

      if (this.calories < 0 || this.walkHours < 0 || this.drinking <0 ) {
        alert("Calories, Walking Hours and Drinking cannot be negative. \n Minimum value 0.0");
        return false;
      }

      return true;
    },
    addDetail() {
      if (!this.isInputValid()) return;
      if(this.id == null) {
        fetch("/api/trackers", {
          method: "POST",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ userId: this.userId, calories: this.calories, drinking:
            this.drinking, walkHours: this.walkHours }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the tracker could not be saved.");
        });
      } else {
        fetch("/api/trackers", {
          method: "PUT",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ id:this.id, userId: this.userId, calories: this.calories,
            drinking: this.drinking, walkHours: this.walkHours }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the tracker could not be saved.");
        });
      }


    },
    updateDetail: function (detail) {
      this.id = detail.id;
      this.calories = detail.calories;
      this.walkHours = detail.walkHours;
      this.drinking = detail.drinking;
    },
    selectInsertMode: function () {
      this.calories = null;
      this.walkHours = null;
      this.drinking = null;
      this.id = null;
    }
  }
});
</script>

