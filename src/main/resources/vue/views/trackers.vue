<template id="trackers">
  <app-layout/>
  <div>
    <div class="row"><br /></div>
    <div class="row">
      <div class="col-md-5">
          <div class="row"><br /></div>
          <div class="row bg-primary">
            <div class="col-md-1">Id</div>
            <div class="col-md-3">Name</div>
            <div class="col-md-5">Email</div>
            <div class="col-md-3">Action</div>
          </div>
          <div class="row bg-success" v-for="user in users" :key="user.id">
            <div class="col-md-1">{{user.id}}</div>
            <div class="col-md-3">{{user.name}}</div>
            <div class="col-md-5">{{user.email}}</div>
            <div class="col-md-3">
              <button @click="fetchDetails(`${user.id}`)">View</button>
            </div>
          </div>
      </div>
      <div class="col-md-1">
        <br />
      </div>
      <div class="col-md-6">
        <div class="row bg-primary" v-show="users.length !== 0 && userId!=null"><b>User Id: {{userId}}</b></div>
        <div class="row"><br /></div>
        <div class="row" v-show="users.length !== 0 && userId!=null">
          Calories: <input v-model="calories" /> Walking:
          <input v-model="walkHours" /> Drinking:
          <input v-model="drinking" />
          <button @click="addDetail()">Add tracking</button>
        </div>
        <div class="row"><br /></div>
        <div class="row" v-show="!showDetails">
          <span v-if="userId!=null"> No Trackers for user id {{userId}}. Please add.</span>
          <span v-else>Please click view button for a user.</span>
        </div>
        <div class="row" v-show="showDetails">
          <div class="row bg-danger">
            <!-- <div class="col-md-1">Id</div> -->
            <div class="col-md-3">Calories</div>
            <div class="col-md-3">Walking</div>
            <div class="col-md-3">Drinking</div>
            <div class="col-md-2">Action</div>
          </div>
          <div class="row bg-success" v-for="detail in details" :key="detail.id">
            <!-- <div class="col-md-1">{{track.id}}</div> -->
            <div class="col-md-3">{{detail.calories}} K.Cal</div>
            <div class="col-md-3">{{detail.walkHours}} KM</div>
            <div class="col-md-3">{{detail.drinking}} L</div>
            <div class="col-md-3">
              <button @click="deleteDetail(`${detail.id}`)">Delete</button>
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
    userId: null
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
            alert("Error while fetching trackers");
            this.showDetails = false;
            this.userId = userId;
          });
    },
    deleteDetail(id) {
      alert(id);
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
    addDetail() {
      if (this.calories === "" || this.calories == null) {
        alert("Calories cannot be black");
        return;
      }
      if (this.walkHours === "" || this.walkHours == null) {
        alert("Walking cannot be black");
        return;
      }
      if (this.drinking === "" || this.drinking == null) {
        alert("Drinking cannot be black");
        return;
      }
      fetch("/api/trackers", {
        method: "POST",
        cache: "no-store",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ userId: this.userId, calories: this.calories, drinking: this.drinking, walkHours: this.walkHours }),
      }).then((response) => {
        alert(response.status);
        this.fetchDetails(this.userId);
      }).catch((err) => {
        alert("There is an error, the tracker could not be saved.");
      });

    }
  }
});
</script>