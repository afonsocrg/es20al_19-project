<template>
  <div v-if="userStats">
    <v-dialog
      :value="dialog"
      @input="$emit('dialog', false)"
      @keydown.esc="$emit('dialog', false)"
      max-width="70%"
    >
      <v-card>
        <v-card-title>
          <span class="headline"
            ><b>{{ student.name }}</b></span
          >
        </v-card-title>

        <v-card-text class="text-left">
          <show-dashboard-stats :stats="userStats" />
        </v-card-text>

        <v-card-actions>
          <v-spacer />
          <v-btn
            data-cy="closeDashboardTable"
            dark
            color="blue darken-1"
            @click="$emit('close-show-dashboard-stats-dialog')"
            >close</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Model, Watch } from 'vue-property-decorator';
import DashboardStats from '@/models/statement/DashboardStats';
import RemoteServices from '@/services/RemoteServices';
import { Student } from '@/models/management/Student';
import ShowDashboardStats from '@/views/student/dashboard/ShowDashboardStats.vue';

@Component({
  components: {
    'show-dashboard-stats': ShowDashboardStats
  }
})
export default class ShowDashboardStatsDialog extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  @Prop(Student) readonly student!: Student;
  @Prop(DashboardStats) readonly userStats: DashboardStats | null = null;
}
</script>
<style lang="scss" scoped></style>
