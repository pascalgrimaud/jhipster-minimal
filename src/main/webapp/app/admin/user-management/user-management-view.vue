<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="userEntity">
        <h2 class="jh-entity-heading">
          <span>User</span> [<b>{{ userEntity.login }}</b
          >]
        </h2>
        <dl class="row jh-entity-details">
          <dt><span>Login</span></dt>
          <dd>
            <span>{{ userEntity.login }}</span>
            <b-badge variant="success" v-if="userEntity.activated">Activated</b-badge>
            <b-badge variant="danger" v-if="!userEntity.activated">Deactivated</b-badge>
          </dd>
          <dt><span>First Name</span></dt>
          <dd>{{ userEntity.firstName }}</dd>
          <dt><span>Last Name</span></dt>
          <dd>{{ userEntity.lastName }}</dd>
          <dt><span>Email</span></dt>
          <dd>{{ userEntity.email }}</dd>
          <dt><span>Created By</span></dt>
          <dd>{{ userEntity.createdBy }}</dd>
          <dt><span>Created Date</span></dt>
          <dd>{{ userEntity.createdDate | formatDate }}</dd>
          <dt><span>Last Modified By</span></dt>
          <dd>{{ userEntity.lastModifiedBy }}</dd>
          <dt><span>Last Modified Date</span></dt>
          <dd>{{ userEntity.lastModifiedDate | formatDate }}</dd>
          <dt><span>Profiles</span></dt>
          <dd>
            <ul class="list-unstyled">
              <li v-for="authorityEntity of userEntity.authorities" :key="authorityEntity">
                <b-badge variant="info">{{ authorityEntity }}</b-badge>
              </li>
            </ul>
          </dd>
        </dl>
        <router-link tag="button" type="submit" :to="{ name: 'JhiUser' }" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./userEntity-management-view.component.ts"></script>
