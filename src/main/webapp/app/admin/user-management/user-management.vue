<template>
  <div>
    <h2>
      <span id="userEntity-management-page-heading" data-cy="userManagementPageHeading">Users</span>

      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isLoading">
          <font-awesome-icon icon="sync" :spin="isLoading"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link tag="button" class="btn btn-primary jh-create-entity" :to="{ name: 'JhiUserCreate' }">
          <font-awesome-icon icon="plus"></font-awesome-icon> <span>Create a new User</span>
        </router-link>
      </div>
    </h2>
    <div class="table-responsive" v-if="userEntities">
      <table class="table table-striped" aria-describedby="Users">
        <thead>
          <tr>
            <th scope="col" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('login')">
              <span>Login</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'login'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('email')">
              <span>Email</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
            <th scope="col"><span>Profiles</span></th>
            <th scope="col" v-on:click="changeOrder('createdDate')">
              <span>Created Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('lastModifiedBy')">
              <span>Last Modified By</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastModifiedBy'"></jhi-sort-indicator>
            </th>
            <th scope="col" id="modified-date-sort" v-on:click="changeOrder('lastModifiedDate')">
              <span>Last Modified Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastModifiedDate'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody v-if="userEntities">
          <tr v-for="userEntity in userEntities" :key="userEntity.id" :id="userEntity.login">
            <td>
              <router-link tag="a" :to="{ name: 'JhiUserView', params: { userId: userEntity.login } }">{{ userEntity.id }}</router-link>
            </td>
            <td>{{ userEntity.login }}</td>
            <td class="jhi-userEntity-email">{{ userEntity.email }}</td>
            <td>
              <button class="btn btn-danger btn-sm deactivated" v-on:click="setActive(userEntity, true)" v-if="!userEntity.activated">
                Deactivated
              </button>
              <button
                class="btn btn-success btn-sm"
                v-on:click="setActive(userEntity, false)"
                v-if="userEntity.activated"
                :disabled="username === userEntity.login"
              >
                Activated
              </button>
            </td>

            <td>
              <div v-for="authorityEntity of userEntity.authorities" :key="authorityEntity">
                <span class="badge badge-info">{{ authorityEntity }}</span>
              </div>
            </td>
            <td>{{ userEntity.createdDate | formatDate }}</td>
            <td>{{ userEntity.lastModifiedBy }}</td>
            <td>{{ userEntity.lastModifiedDate | formatDate }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'JhiUserView', params: { userId: userEntity.login } }"
                  tag="button"
                  class="btn btn-info btn-sm details"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </router-link>
                <router-link
                  :to="{ name: 'JhiUserEdit', params: { userId: userEntity.login } }"
                  tag="button"
                  class="btn btn-primary btn-sm edit"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(userEntity)"
                  variant="danger"
                  class="btn btn-sm delete"
                  :disabled="username === userEntity.login"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <b-modal ref="removeUser" id="removeUser" @ok="deleteUser()">
        <div class="modal-body">
          <p id="jhi-delete-userEntity-heading">Are you sure you want to delete this userEntity?</p>
        </div>
        <div slot="modal-footer">
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button type="button" class="btn btn-primary" id="confirm-delete-userEntity" v-on:click="deleteUser()">Delete</button>
        </div>
      </b-modal>
    </div>
    <div v-show="userEntities && userEntities.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./userEntity-management.component.ts"></script>
