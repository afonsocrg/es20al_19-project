<template>
  <v-dialog
    :value="dialog"
    @input="$emit('close-edit-student-question-dialog', false)"
    @keydown.esc="$emit('close-edit-student-question-dialog', false)"
    max-width="75%"
    max-height="95%"
    scrollable
  >
    <v-card>
      <v-card-title>
        <span class="headline">
          {{
            editStudentQuestion && editStudentQuestion.id === null
              ? 'New Question'
              : 'Edit My Question'
          }}
        </span>
      </v-card-title>

      <v-card-text
        class="text-left"
        v-if="editStudentQuestion"
        data-cy="Topics"
      >
        <v-container grid-list-md fluid>
          <v-layout column wrap>
            <v-form>
              <v-autocomplete
                v-model="studentQuestionTopics"
                label="Topics"
                :items="topics"
                multiple
                return-object
                item-text="name"
                item-value="name"
              >
                <template v-slot:selection="data">
                  <v-chip
                    v-bind="data.attrs"
                    :input-value="data.selected"
                    close
                    @click="data.select"
                    @click:close="removeTopic(data.item)"
                    :data-cy="data.item.name"
                  >
                    {{ data.item.name }}
                  </v-chip>
                </template>
                <template v-slot:item="data">
                  <v-list-item-content data-cy="topicList">
                    <v-list-item-title v-html="data.item.name" />
                  </v-list-item-content>
                </template>
              </v-autocomplete>
            </v-form>
            <v-flex xs24 sm12 md8>
              <v-text-field
                v-model="editStudentQuestion.title"
                label="Question Title"
                data-cy="StudentQuestionTitle"
              />
            </v-flex>
            <v-flex xs24 sm12 md8>
              <span>Question Content</span>
              <ckeditor
                outline
                auto-grow
                rows="1"
                v-model="editStudentQuestion.content"
                label="Question"
                data-cy="StudentQuestionContent"
                :config="editorConfig"
              ></ckeditor>
            </v-flex>
            <v-flex
              xs24
              sm12
              md8
              v-for="index in editStudentQuestion.options.length"
              :key="index"
            >
              <v-switch
                v-model="editStudentQuestion.options[index - 1].correct"
                class="ma-4"
                label="Correct"
                :data-cy="`CorrectOption${index}`"
              />
              <span>Option {{ index }}</span>
              <ckeditor
                outline
                auto-grow
                rows="1"
                v-model="editStudentQuestion.options[index - 1].content"
                :data-cy="`Option${index}`"
                :config="editorConfig"
              ></ckeditor>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-text>

      <v-card-actions v-if="!isMobile">
        <v-spacer />
        <v-btn
          color="error"
          @click="$emit('close-edit-student-question-dialog', false)"
          data-cy="CancelStudentQuestion"
          >Cancel</v-btn
        >
        <v-btn
          color="primary"
          dark
          @click="saveStudentQuestion"
          data-cy="SaveStudentQuestion"
        >
          Save
        </v-btn>
      </v-card-actions>
      <v-card-actions v-else>
        <v-spacer />
        <v-btn fab color="primary" small @click="saveStudentQuestion">
          <v-icon medium class="mr-2">
            far fa-save
          </v-icon>
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue } from 'vue-property-decorator';
import StudentQuestion from '@/models/management/StudentQuestion';
import Topic from '@/models/management/Topic';
import RemoteServices from '@/services/RemoteServices';

@Component
export default class EditStudentQuestionDialog extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  @Prop({ type: StudentQuestion, required: true })
  studentQuestion!: StudentQuestion;
  @Prop({ type: Array, required: true }) readonly topics!: Topic[];
  @Prop({ type: Boolean, required: true }) readonly isMobile!: boolean;

  studentQuestionTopics: Topic[] = JSON.parse(
    JSON.stringify(this.studentQuestion.topics)
  );

  editStudentQuestion!: StudentQuestion;

  created() {
    this.editStudentQuestion = new StudentQuestion(this.studentQuestion);
  }

  data() {
    return {
      editorConfig: {
        language: 'en',
        extraPlugins: 'mathjax',
        mathJaxLib:
          'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-AMS_HTML'
      }
    };
  }

  async saveStudentQuestion() {
    if (
      this.editStudentQuestion &&
      (!this.editStudentQuestion.title ||
        !this.editStudentQuestion.content ||
        !this.studentQuestionTopics)
    ) {
      await this.$store.dispatch(
        'error',
        'Question must have title, content, and at least one topic'
      );
      return;
    }
    this.editStudentQuestion.topics = this.studentQuestionTopics;

    if (this.editStudentQuestion && this.editStudentQuestion.id != null) {
      try {
        const result = await RemoteServices.updateStudentQuestion(
          this.editStudentQuestion
        );
        this.$emit('save-student-question', result);
      } catch (error) {
        await this.$store.dispatch('error', error);
      }
    } else if (this.editStudentQuestion) {
      try {
        const result = await RemoteServices.createStudentQuestion(
          this.editStudentQuestion
        );
        this.$emit('save-student-question', result);
      } catch (error) {
        await this.$store.dispatch('error', error);
      }
    }
  }

  removeTopic(topic: Topic) {
    this.studentQuestionTopics = this.studentQuestionTopics.filter(
      element => element.id != topic.id
    );
  }
}
</script>
